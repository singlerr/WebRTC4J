cmake_minimum_required(VERSION 3.13)

if(NOT WEBRTC_TOOLCHAIN_FILE STREQUAL "")
	message(STATUS "Toolchain file: ${WEBRTC_TOOLCHAIN_FILE}")
	set(CMAKE_TOOLCHAIN_FILE "${WEBRTC_TOOLCHAIN_FILE}")
endif()


project(webrtc-java)

add_subdirectory(webrtc)