package com.smarthire.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.smarthire.entity.Application;
import com.smarthire.entity.CandidateProfile;
import com.smarthire.entity.Skill;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfReportService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    public byte[] generateApplicationSummaryPdf(Application application) {
        Document document = new Document(PageSize.A4, 36, 36, 40, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Header
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.BLACK);
            Paragraph title = new Paragraph("SmartHire - Application Summary", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15);
            document.add(title);

            // Subtitle
            Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.DARK_GRAY);
            Paragraph sub = new Paragraph("Generated on " + java.time.LocalDate.now().format(DATE_FMT), subFont);
            sub.setAlignment(Element.ALIGN_CENTER);
            sub.setSpacingAfter(25);
            document.add(sub);

            // Table of Details
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{30f, 70f});

            addTableRow(table, "Application ID", "#" + application.getId());
            addTableRow(table, "Job Title", application.getJob().getTitle());
            addTableRow(table, "Company", application.getJob().getCompany().getName());
            addTableRow(table, "Location", application.getJob().getLocation() + " (" + application.getJob().getWorkMode() + ")");
            addTableRow(table, "Candidate Name", application.getCandidate().getFullName());
            addTableRow(table, "Candidate Email", application.getCandidate().getEmail());
            addTableRow(table, "Current Status", application.getStatus().name());
            addTableRow(table, "Applied Date", application.getAppliedAt().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")));

            if (application.getRecruiterNotes() != null) {
                addTableRow(table, "Recruiter Notes", application.getRecruiterNotes());
            }

            if (application.getRejectionReason() != null) {
                addTableRow(table, "Rejection Reason", application.getRejectionReason());
            }

            document.add(table);

            if (application.getCoverLetter() != null && !application.getCoverLetter().isBlank()) {
                Paragraph clHeader = new Paragraph("\nCover Letter", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
                clHeader.setSpacingBefore(15);
                clHeader.setSpacingAfter(10);
                document.add(clHeader);

                Paragraph clBody = new Paragraph(application.getCoverLetter(), FontFactory.getFont(FontFactory.HELVETICA, 11));
                document.add(clBody);
            }

            document.close();
        } catch (DocumentException ex) {
            throw new RuntimeException("Error generating PDF summary", ex);
        }

        return out.toByteArray();
    }

    public byte[] generateCandidateProfilePdf(CandidateProfile profile) {
        Document document = new Document(PageSize.A4, 36, 36, 40, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Header
            Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, new Color(15, 23, 42));
            Paragraph name = new Paragraph(profile.getUser().getFullName(), nameFont);
            name.setSpacingAfter(4);
            document.add(name);

            Font contactFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
            String contactInfo = profile.getUser().getEmail() + 
                    (profile.getPhone() != null ? " | " + profile.getPhone() : "") +
                    (profile.getLocation() != null ? " | " + profile.getLocation() : "");
            Paragraph contact = new Paragraph(contactInfo, contactFont);
            contact.setSpacingAfter(20);
            document.add(contact);

            if (profile.getProfessionalSummary() != null && !profile.getProfessionalSummary().isBlank()) {
                Paragraph sHeader = new Paragraph("Professional Summary", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(30, 41, 59)));
                sHeader.setSpacingAfter(5);
                document.add(sHeader);
                Paragraph sBody = new Paragraph(profile.getProfessionalSummary(), FontFactory.getFont(FontFactory.HELVETICA, 10));
                sBody.setSpacingAfter(15);
                document.add(sBody);
            }

            if (profile.getSkills() != null && !profile.getSkills().isEmpty()) {
                Paragraph skHeader = new Paragraph("Key Skills", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(30, 41, 59)));
                skHeader.setSpacingAfter(5);
                document.add(skHeader);
                String skills = profile.getSkills().stream().map(Skill::getName).reduce((a, b) -> a + ", " + b).orElse("");
                Paragraph skBody = new Paragraph(skills, FontFactory.getFont(FontFactory.HELVETICA, 10));
                skBody.setSpacingAfter(15);
                document.add(skBody);
            }

            document.close();
        } catch (DocumentException ex) {
            throw new RuntimeException("Error generating profile PDF", ex);
        }

        return out.toByteArray();
    }

    private void addTableRow(PdfPTable table, String label, String value) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK)));
        cell1.setBackgroundColor(new Color(241, 245, 249));
        cell1.setPadding(8);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase(value != null ? value : "N/A", FontFactory.getFont(FontFactory.HELVETICA, 10)));
        cell2.setPadding(8);
        table.addCell(cell2);
    }
}
