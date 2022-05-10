################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/server/ReceiveDatagramThread.cpp \
../src/server/ServerMain.cpp 

CPP_DEPS += \
./src/server/ReceiveDatagramThread.d \
./src/server/ServerMain.d 

OBJS += \
./src/server/ReceiveDatagramThread.o \
./src/server/ServerMain.o 


# Each subdirectory must supply rules for building sources it contributes
src/server/%.o: ../src/server/%.cpp src/server/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -Wno-literal-suffix -DBOOST_BIND_GLOBAL_PLACEHOLDERS -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src-2f-server

clean-src-2f-server:
	-$(RM) ./src/server/ReceiveDatagramThread.d ./src/server/ReceiveDatagramThread.o ./src/server/ServerMain.d ./src/server/ServerMain.o

.PHONY: clean-src-2f-server

