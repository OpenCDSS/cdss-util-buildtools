#!/bin/sh
(set -o igncr) 2>/dev/null && set -o igncr; # This comment is required.
# The above line ensures that the script can be run on Cygwin/Linux even with Windows CRNL.
#
# Install these files into the NSIS 3.08 software installation folder:
# - the destination folder is expected to be C:\Program Files (x86)\NSIS
# - this script is written to run in Git Bash

# Determine the OS that is running the script:
# - mainly care whether Cygwin
getOperatingSystem() {
  if [ ! -z "${operatingSystem}" ]
  then
    # Have already checked operating system so return.
    return
  fi
  operatingSystem="unknown"
  case "$(uname)" in
    CYGWIN*) operatingSystem="cygwin";;
    MINGW*) operatingSystem="mingw";;
  esac
  echo "operatingSystem=${operatingSystem}"
  if [ ! "${operatingSystem}" = "mingw" ]
  then
    echo "This script is written to run in Git Bash (current shell is ${operatingSystem}).  Exiting."
    exit 1
  fi
}

# Main entry point into script.

# Determine the operating system and exit if not MinGW for Git Bash.
getOperatingSystem

# Check for NSIS 3.08 installation.
nsisInstallFolder='/c/Program Files (x86)/NSIS'

# There does not seem to be any way to programatically check the NSIS version:
# - no ndis /version or --version?
# - no 3.08 in any files?
# - therefore just print a message

echo ""
echo "You must run this script from a shell opened as Administrator.  Otherwise, errors may result."
echo "Starting NSIS Windows client - check that it is version 3.08 (or later) and then exit."
echo ""
sleep 2
if [ ! -f "${nsisInstallFolder}/NSIS.exe" ]; then
  echo "File does not exist: ${nsisInstallFolder}/NSIS.exe"
  echo "Exiting."
  exit 1
fi
"${nsisInstallFolder}/NSIS.exe"
echo ""
echo "Kill this script with Ctrl-c if necessary and run as Administrator."
echo ""
read -p "Are you sure you want to install on top of NSIS 3.08 (or later) [y/n]? " answer

if [ ! "${answer}" = "y" ]; then
  echo "Exiting"
  exit 0
fi

# Copy the files from this repository to the NSIS 3.08 installation folder:
# - do a recursive copy on all archived files

if [ ! -d "${nsisInstallFolder}" ]; then
  echo ""
  echo "NSIS installation folder does not exist:  ${nsisInstallFolder}"
  echo "Exiting"
  exit 1
fi

echo "Copying Contrib to ${nsisInstallFolder}/Contrib"
cp -r Contrib/* "${nsisInstallFolder}/Contrib"

echo "Copying Include to ${nsisInstallFolder}/Include"
cp -r Include/* "${nsisInstallFolder}/Include"

echo "Copying Plugins to ${nsisInstallFolder}/Plugins"
cp -r Plugins/* "${nsisInstallFolder}/Plugins"

exit 0
