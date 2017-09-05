
CHISELLIB_DIR := $(dir $(lastword $(MAKEFILE_LIST)))

ifneq (1,$(RULES))

CHISELLIB := chisellib.jar
CHISELLIB_SRC := $(wildcard $(CHISELLIB_DIR)/src/chisellib/*.scala)

else # Rules

$(CHISELLIB) : $(CHISEELIB_SRC)
	$(Q)$(CHISELC) -o $@ $(CHISELLIB_SRC)
	$(Q)touch $@
	
endif

