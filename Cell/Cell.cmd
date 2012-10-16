@echo off
rem Copyright © 2012, Michael J Allen.  All rights reserved
rem============================================================================
rem $Id$
rem
rem Windows command prompt to execute the LinkedInContacts application.
rem============================================================================

set CELL_DIR=%~dp0
java -jar %CELL_DIR%\target\Cell-0.0.1-SNAPSHOT.jar %*
