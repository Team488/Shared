ipmo .\Modules\PSFTP\PSFTP.psm1

# Configuration
$gitPath = "SmartGitHg 4.6\git\bin\"
$propertiesFilePath = "Properties.txt"
$ftpPath = "ftp://10.4.88.2"

function GetProgramFiles() {
    $x86 = "C:\Program Files (x86)"
    $x64 = "C:\Program Files"
    
    if ([IO.Directory]::Exists($x86)) {
        return $x86
    }
    
    return $x64
}

function GetGitPath() {
    $programFiles = GetProgramFiles
    $combined = [IO.Path]::Combine($programFiles, $gitPath)
    return $combined
}

$env:PATH += ";$(GetGitPath)"

function ConnectToRobot() {
    $paswd = "password" | ConvertTo-SecureString -AsPlainText -Force
    $cred = New-Object System.Management.Automation.PSCredential "username", $paswd

    Set-FTPConnection -Credentials $cred -Server $ftpPath -Session MyTestSession -UsePassive 
    $session = Get-FTPConnection -Session MyTestSession
    return $session
}

$session = ConnectToRobot
