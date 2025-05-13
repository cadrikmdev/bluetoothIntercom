# BluetoothIntercom android lib

This project meant to be as a library to maintain mutual code of bluetooth communication between worker and management devices (2-way communication) 

## TODO:

BLE implementation is not ready

## Usage:

Create empty 'local.properties' file in root of the module 

To use this lib you can use the default classes in intercom module, but be sure to override following at least: 

in your settings gradle add those 2 line to make it buildable correctly:

project(":intercom:data").projectDir = file("<imported_project_directory>/intercom/data")
project(":intercom:domain").projectDir = file("<imported_project_directory>/intercom/domain")
e.g.
project(":intercom:data").projectDir = file("bluetoothIntercom/intercom/data")
project(":intercom:domain").projectDir = file("bluetoothIntercom/intercom/domain")
if <imported_project_directory> is bluetoothIntercom

then use ":intercom:domain" and ":intercom:domain" to reference it from other models

**BluetoothServiceSpecification** to use your own unique name to recognize the service