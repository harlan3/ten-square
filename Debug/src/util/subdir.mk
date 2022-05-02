################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/util/Base64.cpp 

CPP_DEPS += \
./src/util/Base64.d 

OBJS += \
./src/util/Base64.o 


# Each subdirectory must supply rules for building sources it contributes
src/util/%.o: ../src/util/%.cpp src/util/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src-2f-util

clean-src-2f-util:
	-$(RM) ./src/util/Base64.d ./src/util/Base64.o

.PHONY: clean-src-2f-util

