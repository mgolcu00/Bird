@echo off
set /p branch=Lutfen branch yazin (SO,SK,BM): 
cls
git reset
git checkout master
git pull
git checkout %branch%