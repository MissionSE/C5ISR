#!/usr/bin/env bash

##############################################################################
##
##  Maven Library Push script
##
##############################################################################

function echoc() {
    prompt="$1"
    echo -e -n "\033[1;36m$prompt"
    echo -e '\033[0m'
}

origDir=`pwd`

if [ ! -f $HOME/.gradle/gradle.properties ]; then
	cp gradle.properties $HOME/.gradle
fi

thirdPartyLibraries=(
	'third-party/Rajawali'
	'third-party/DroidAR'
	'third-party/ARMarker'
	'third-party/ColorPickerPreference'
	)

libraries=(
	'libraries/AugmentedReality/library'
	'libraries/BluetoothConnector/library'
	'libraries/GestureDetector/library'
	'libraries/HttpDatabaseConnector/library'
	'libraries/ImageViewer/library'
	'libraries/MapViewer/library'
	'libraries/ModelViewer/library'
	'libraries/NfcConnector/library'
	'libraries/UIExtensions/library'
	'libraries/VideoViewer/library'
	'libraries/WifiDirectConnector/library'
	)

for DIR in "${thirdPartyLibraries[@]}"
do
	cd "$DIR"
	echoc "Unzipping archives in $DIR..."
	unzip -uq '*.zip'
	echoc "DONE"
	echo
	echoc "Running gradle tasks clean, build, and install in $DIR..."
	./gradlew -uq clean build install
	echoc "DONE"
	cd $origDir
	echo
done

for DIR in "${libraries[@]}"
do
	cd "$DIR"
	echoc "Running gradle tasks clean, build, and install in $DIR..."
	../gradlew -q clean build install
	echoc "DONE"
	cd $origDir
	echo
done
