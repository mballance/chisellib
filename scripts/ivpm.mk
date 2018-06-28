
CHISELLIB_SCRIPTS_DIR := $(abspath $(dir $(lastword $(MAKEFILE_LIST))))
CHISELLIB_DIR := $(abspath $(CHISELLIB_SCRIPTS_DIR)/..)
PACKAGES_DIR ?= $(CHISELLIB_DIR)/packages
LIB_DIR = $(CHISELLIB_DIR)/lib

# Must support dual modes: 
# - build dependencies if this project is the active one
# - rely on the upper-level makefile to resolve dependencies if we're not
-include $(PACKAGES_DIR)/packages.mk
include $(CHISELLIB_DIR)/etc/ivpm.info

include $(CHISELLIB_DIR)/mkfiles/chisellib.mk
include $(PACKAGES_DIR)/chiselscripts/mkfiles/chiselscripts.mk

CHISELLIB_SRC := \
	$(wildcard $(CHISELLIB_DIR)/src/chisellib/*.scala) \
	$(wildcard $(CHISELLIB_DIR)/src/chisellib/factory/*.scala) \
	$(wildcard $(CHISELLIB_DIR)/src/chisellib/blackbox/*.scala) \

RULES := 1

ifeq (true,$(PHASE2))
build : $(CHISELLIB_JAR)

clean : 
	$(Q)rm -rf $(LIB_DIR)
else
build : $(chisellib_deps)
	$(MAKE) -f $(lastword $(MAKEFILE_LIST)) PHASE2=true build

clean : clean_chisellib

endif

$(CHISELLIB_JAR) : $(CHISELLIB_SRC)
	$(Q)if test ! -d `dirname $@`; then mkdir -p `dirname $@`; fi
	$(Q)$(DO_CHISELC)

release : build
	$(Q)rm -rf $(CHISELLIB_DIR)/build
	$(Q)mkdir -p $(CHISELLIB_DIR)/build/chisellib
	$(Q)cp -r \
          $(CHISELLIB_DIR)/lib \
          $(CHISELLIB_DIR)/etc \
          $(CHISELLIB_DIR)/build/chisellib
	$(Q)cd $(CHISELLIB_DIR)/build ; \
		tar czf chisellib-$(version).tar.gz chisellib
	$(Q)rm -rf $(CHISELLIB_DIR)/build/chisellib

include $(CHISELLIB_DIR)/mkfiles/chisellib.mk
include $(PACKAGES_DIR)/chiselscripts/mkfiles/chiselscripts.mk
-include $(PACKAGES_DIR)/packages.mk

