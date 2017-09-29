
CHISELLIB_SRC_DIR := $(dir $(lastword $(MAKEFILE_LIST)))

ifneq (1,$(RULES))

CHISELLIB_JAR := chisellib.jar
CHISELLIB_SRC := \
	$(wildcard $(CHISELLIB_SRC_DIR)/chisellib/*.scala) \
	$(wildcard $(CHISELLIB_SRC_DIR)/chisellib/blackbox/*.scala) \

else # Rules

$(CHISELLIB_JAR) : $(CHISELLIB_SRC)
	$(Q)$(CHISELC) -o $@ $(CHISELLIB_SRC)
	$(Q)touch $@
	
endif

