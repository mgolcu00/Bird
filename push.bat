@echo off

set /p commit=Lutfen commit aciklamasi yazin:
cls
git add .
git add .
git commit -m "%commit%"
git push origin master
