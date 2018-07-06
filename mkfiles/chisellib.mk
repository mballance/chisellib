
CHISELLIB_MKFILES_DIR := $(dir $(lastword $(MAKEFILE_LIST)))
CHISELLIB_DIR := $(abspath $(CHISELLIB_MKFILES_DIR)/..)

ifneq (1,$(RULES))

CHISELLIB_JAR = $(CHISELLIB_DIR)/lib/chisellib.jar

CHISELLIB_JARS = $(CHISELLIB_JAR)

else # Rules

	
endif

