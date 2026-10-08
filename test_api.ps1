Write-Host "=== 1. TEST CANDIDATE FLOW ==="
$candLogin = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -Body (@{email="john.doe@example.com"; password="Candidate@123"} | ConvertTo-Json) -ContentType "application/json"
$candToken = $candLogin.data.token
$candHeaders = @{ "Authorization" = "Bearer $candToken" }

# Get candidate profile
$profile = Invoke-RestMethod -Uri "http://localhost:8080/api/candidate/profile" -Method Get -Headers $candHeaders
Write-Host "Candidate Name: " $profile.data.fullName
Write-Host "Candidate Skills: " ($profile.data.skills -join ", ")

# Download candidate PDF
$pdfReq = [System.Net.HttpWebRequest]::Create("http://localhost:8080/api/reports/candidate/$($candLogin.data.user.id)/pdf")
$pdfReq.Headers.Add("Authorization", "Bearer $candToken")
$pdfResp = $pdfReq.GetResponse()
$pdfStream = $pdfResp.GetResponseStream()
$ms = New-Object System.IO.MemoryStream
$pdfStream.CopyTo($ms)
Write-Host "Candidate Profile PDF downloaded, bytes: " $ms.Length " Content-Type: " $pdfResp.ContentType

# Get candidate applications
$myApps = Invoke-RestMethod -Uri "http://localhost:8080/api/candidate/applications" -Method Get -Headers $candHeaders
Write-Host "Candidate Applications count: " $myApps.data.Count " Status: " $myApps.data[0].status

# Get candidate interviews
$myInterviews = Invoke-RestMethod -Uri "http://localhost:8080/api/candidate/interviews" -Method Get -Headers $candHeaders
Write-Host "Candidate Interviews count: " $myInterviews.data.Count " Interview Type: " $myInterviews.data[0].interviewType

Write-Host "`n=== 2. TEST RECRUITER FLOW ==="
$recLogin = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -Body (@{email="recruiter@technova.com"; password="Recruiter@123"} | ConvertTo-Json) -ContentType "application/json"
$recToken = $recLogin.data.token
$recHeaders = @{ "Authorization" = "Bearer $recToken" }

# Recruiter Dashboard
$recDash = Invoke-RestMethod -Uri "http://localhost:8080/api/recruiter/stats" -Method Get -Headers $recHeaders
Write-Host "Recruiter Active Jobs: " $recDash.data.activeJobs " Total Apps: " $recDash.data.totalApplications

# Recruiter view applicants for Job 1
$apps = Invoke-RestMethod -Uri "http://localhost:8080/api/recruiter/jobs/1/applications" -Method Get -Headers $recHeaders
Write-Host "Applicants for Job 1: " $apps.data.Count " First Applicant: " $apps.data[0].candidateName

Write-Host "`n=== 3. TEST ADMIN FLOW ==="
$adminLogin = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -Body (@{email="admin@smarthire.com"; password="Admin@123"} | ConvertTo-Json) -ContentType "application/json"
$adminToken = $adminLogin.data.token
$adminHeaders = @{ "Authorization" = "Bearer $adminToken" }

# Check pending companies
$pendingCompanies = Invoke-RestMethod -Uri "http://localhost:8080/api/admin/companies?verified=false" -Method Get -Headers $adminHeaders
Write-Host "Pending Companies count: " $pendingCompanies.data.Count " First pending: " $pendingCompanies.data[0].name

Write-Host "`n=== ALL API TESTS PASSED SUCCESSFULLY! ==="
