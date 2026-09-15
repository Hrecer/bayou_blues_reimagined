@echo off
rem 双击启动 Minecraft 开发客户端（Forge 1.20.1 + Bayou Blues + Quark）
rem 首次运行或依赖变更后 Gradle 会先编译，需要等待；窗口保持打开以便查看报错。
cd /d "%~dp0"
call gradlew.bat runClient %*
echo.
echo 游戏已退出（退出码 %ERRORLEVEL%）。如需查看详细日志请打开 run\logs\latest.log
pause
