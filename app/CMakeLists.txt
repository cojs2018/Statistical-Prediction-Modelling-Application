#CMakeLists.txt : CMake project for Statistical Prediction Modelling Application, include source
# and define project specific logic here
#

cmake_required_required (VERSION 3.8)

project(StatApp VERSION 1.0)

#Add source to this projects executable.
add_executable (LinProgSol "LinProgSol.cpp" "LinProgSol.h")
add_executable (ArffData "Arff_data.cpp" "Arff_data.h")

add_library(ArffData SHARED "Arff_data.h")

target_link_libraries(StatApp PUBLIC Eigen3)

#Search for Eigen3
target_include_directories(StatApp PUBLIC
                           "${Eigen3_BINARY_DIR}"
                           "${Eigen3_SOURCE_DIR}/Eigen3"
                           )

#TODO: Add tests and install targets if needed

