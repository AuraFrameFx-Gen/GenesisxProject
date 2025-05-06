cmake_minimum_required(VERSION 3.18.1)

# Define the project name
project("AuraFrameFX")

# Add your native library
add_library(
        # Sets the name of the library
        aura-fx-lib

        # Sets the library as a shared library
        SHARED

        # Provides a relative path to your source file(s)
        src/main/cpp/aura_fx_lib.cpp
)

# Specifies a path to native header files
include_directories(src/main/cpp/)

# Links the libraries you're using against
find_library(
        log-lib
        log)

target_link_libraries(
        aura-fx-lib
        ${log-lib})
