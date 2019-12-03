@echo off

set /p commit=Lutfen commit aciklamasi yazin:
set /p branch=Lutfen branch yazin (SO,SK,BM): 
cls
git add .
git add .
git commit -m "%commit%"
git push origin %branch%
