package com.smarthire.notification;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smarthire.dto.NotificationDTO;
import com.smarthire.entity.Notification;
import com.smarthire.entity.User;
import com.smarthire.exception.ResourceNotFoundException;
import com.smarthire.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private JavaMailSender mailSender;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Value("${smarthire.mail.simulation-mode:true}")
    private boolean simulationMode;

    @Transactional
    public void sendNotification(User recipient, String title, String message, String link) {
        // Save in-app notification
        Notification notification = Notification.builder()
                .recipient(recipient)
                .title(title)
                .message(message)
                .link(link)
                .isRead(false)
                .build();
        notificationRepository.save(notification);

        // Dispatch email notification
        sendEmailAsync(recipient.getEmail(), title, message);
    }

    @Async
    public void sendEmailAsync(String toEmail, String subject, String body) {
        if (simulationMode || mailSender == null) {
            log.info("========== [EMAIL SIMULATION] ==========");
            log.info("TO: {}", toEmail);
            log.info("SUBJECT: {}", subject);
            log.info("BODY: {}", body);
            log.info("=========================================");
            return;
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(toEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            mailSender.send(mailMessage);
            log.info("Email sent successfully to {}", toEmail);
        } catch (Exception ex) {
            log.warn("Failed to dispatch real SMTP email to {}. Falling back to log notice: {}", toEmail, ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getUserNotifications(User user) {
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(user).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(User user) {
        return notificationRepository.countByRecipientAndIsReadFalse(user);
    }

    @Transactional
    public void markAsRead(Long notificationId, User user) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + notificationId));

        if (!notification.getRecipient().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Cannot update another user's notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(User user) {
        List<Notification> unread = notificationRepository.findByRecipientAndIsReadFalseOrderByCreatedAtDesc(user);
        for (Notification n : unread) {
            n.setRead(true);
        }
        notificationRepository.saveAll(unread);
    }

    private NotificationDTO mapToDTO(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .isRead(n.isRead())
                .link(n.getLink())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
