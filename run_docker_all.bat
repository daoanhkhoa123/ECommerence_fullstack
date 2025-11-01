@echo off
setlocal enabledelayedexapnsion

set ACTION=up
set TARGET=

for %%A in (%*) do (
    if /I "%%A"=="up" set ACTION=up
    if /I "%%A"=="down" set ACTION=down
    if /I "%%A"=="rebuild" set ACTION=rebuild
    if /I not "%%A"=="up" if /I not "%%A"=="down" if /I not "%%A"=="rebuild" set TARGET=!TARGET! %%A
)

echo 