@echo off
setlocal EnableDelayedExpansion

set PLATFORM=windows-x86_64
set TARGET_CPU=x64

if not exist "build\%PLATFORM%" (
    mkdir "build\%PLATFORM%"
)

cd "build\%PLATFORM%"

if not exist "depot_tools.zip" (
    echo depot_tools does not exist! Downloading..
    curl -L "https://storage.googleapis.com/chrome-infra/depot_tools.zip" -o "depot_tools.zip"
)

echo Checking hash...
echo 2b9cea54b15f747153dbcc844339d4da29a86b29e806f97e38e2a5ecdc4d40c2  depot_tools.zip | certutil -hashfile depot_tools.zip SHA256

if %errorlevel% neq 0 (
    echo Checksum is not valid. Re-downloading depot_tools...
    del "depot_tools.zip"
    curl -L "https://storage.googleapis.com/chrome-infra/depot_tools.zip" -o "depot_tools.zip"
)

if not exist "depot_tools" (
    echo Decompressing archives...
    powershell -Command "Expand-Archive -Path depot_tools.zip -DestinationPath depot_tools"
)

set DEPOT_TOOLS=%CD%\depot_tools
set PATH=%DEPOT_TOOLS%;%PATH%

if not exist "webrtc" (
    mkdir "webrtc"
)

cd webrtc

set COMPILE_ARGS="is_debug=false treat_warnings_as_errors=false rtc_build_examples=false rtc_include_tests=false use_rtti=true use_custom_libcxx=false symbol_level=0 rtc_use_h264=true"
set BUILD_OUT=out/%TARGET_CPU%
set WEBRTC_LIB_PATH=%BUILD_OUT%/obj
set WEBRTC_LIB_NAME=webrtc.lib

if "%PLATFORM%"=="windows-x86" (
    set DEPOT_TOOLS_WIN_TOOLCHAIN=0
    set GYP_MSVS_VERSION=2019
    set GYP_GENERATORS=ninja,msvs-ninja
    echo WebRTC: fetch
    cmd /c "fetch --nohooks webrtc"
    echo WebRTC: sync
    cmd /c "gclient sync -D"
    echo WebRTC: generate
    cd src
    cmd /c "gn gen %BUILD_OUT% --args=%COMPILE_ARGS%"
    echo WebRTC: compile
    cmd /c "ninja -C %BUILD_OUT%"
) else if "%PLATFORM%"=="windows-x86_64" (
    set DEPOT_TOOLS_WIN_TOOLCHAIN=0
    set GYP_MSVS_VERSION=2019
    set GYP_GENERATORS=ninja,msvs-ninja
    echo WebRTC: fetch
    cmd /c "fetch --nohooks webrtc"
    echo WebRTC: sync
    cmd /c "gclient sync -D"
    echo WebRTC: generate
    cd src
    cmd /c "gn gen %BUILD_OUT% --args=%COMPILE_ARGS%"
    echo WebRTC: compile
    cmd /c "ninja -C %BUILD_OUT%"
) else (
    echo Error: Platform "%PLATFORM%" is not supported
)
