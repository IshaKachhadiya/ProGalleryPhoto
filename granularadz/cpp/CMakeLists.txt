cmake_minimum_required(VERSION 3.18.1)

add_library(dogstrorm SHARED
        dogstrorm.c)

# Include libraries needed for tenoku lib
target_link_libraries(dogstrorm
        android
        log)
