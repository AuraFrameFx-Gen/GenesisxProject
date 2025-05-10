# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.22.1)

# Declares and names the project.
project("AuraFrameFX")

# Specifies the library to be created.
add_library(
        # Sets the name of the library.
        AuraFrameFX

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        AuraFrameFX.cpp
)

# Searches for a specified prebuilt library and stores the path as a variable.
find_library(
        # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that you want CMake to locate.
        log
)

# Specifies libraries CMake should link to your target library. You can link multiple
# libraries, such as libraries you define in this build script, prebuilt third-party
# libraries, or system libraries.
target_link_libraries(
        # Specifies the target library.
        AuraFrameFX

        # Links the target library to the log library included in the NDK.
        ${log-lib}
)

# Include directories
include_directories(
        src/main/cpp/include
)

# Add compiler flags
target_compile_options(
        AuraFrameFX
        PRIVATE
        -Wall
        -Werror
        -fexceptions
        -frtti
        -fno-limit-debug-info
)

# Set C++ standard
target_compile_features(
        AuraFrameFX
        PRIVATE
        cxx_std_17
)

# Set output directory
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/outputs/${ANDROID_ABI})

# Copy the shared library to the assets directory
add_custom_command(
        TARGET AuraFrameFX
        POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy
        ${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/libAuraFrameFX.so
        ${CMAKE_SOURCE_DIR}/src/main/assets/libAuraFrameFX.so
)
