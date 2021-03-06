$ErrorActionPreference="Stop"
Start-Transcript

ipmo .\Modules\PSFTP\PSFTP.psm1

$paswd = "password" | ConvertTo-SecureString -AsPlainText -Force
$cred = New-Object System.Management.Automation.PSCredential "username", $paswd

function Connect() {
    Set-FTPConnection -Credentials $cred -Server ftp://10.4.88.2 -Session MyTestSession -UsePassive 
    $Session = Get-FTPConnection -Session MyTestSession
    return $Session
}

function RetrieveLogs($Session) {
    $logItems = Get-FTPChildItem -Session $Session -Path / -Filter "Log*" 
    $telItems = Get-FTPChildItem -Session $Session -Path / -Filter "Tel*"
    $items = @($logItems) + @($telItems)
    
    if ($items -ne $null) {
        $items | Get-FTPItem -Session $Session -LocalPath $(pwd)
        $items | Remove-FTPItem -Session $Session
    }
}

while ($true) {
    try
    {
        $session = Connect
        while ($true) {
            RetrieveLogs $session
            Start-Sleep -Seconds 10
        }
    } catch [Exception] {
        Write-Host "Got exception:"
        Write-Host $_.Exception.ToString()
    }
    Start-Sleep -Seconds 10
}

Stop-Transcript