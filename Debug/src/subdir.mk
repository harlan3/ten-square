################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/AppConfig.cpp \
../src/BMP_File_Loader.cpp \
../src/JSONObject.cpp \
../src/SharedData.cpp \
../src/main.cpp 

CPP_DEPS += \
./src/AppConfig.d \
./src/BMP_File_Loader.d \
./src/JSONObject.d \
./src/SharedData.d \
./src/main.d 

OBJS += \
./src/AppConfig.o \
./src/BMP_File_Loader.o \
./src/JSONObject.o \
./src/SharedData.o \
./src/main.o 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp src/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src

clean-src:
	-$(RM) ./src/AppConfig.d ./src/AppConfig.o ./src/BMP_File_Loader.d ./src/BMP_File_Loader.o ./src/JSONObject.d ./src/JSONObject.o ./src/SharedData.d ./src/SharedData.o ./src/main.d ./src/main.o

.PHONY: clean-src

