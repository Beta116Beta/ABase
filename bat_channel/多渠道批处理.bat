::˫ð�ű�ʾע��(setlocal enabledelayedexpansion���ӳٱ�����ֵʹ��)
@echo off&setlocal enabledelayedexpansion
::��ɾ���ļ��У��ٴ����ļ���
for %%i in (*.apk) do (
  ::~ni ��ʾ�޺�׺�ļ���
  if exist %~dp0\%%~ni (
     echo %%~ni�ļ����Ѵ���,ִ�����
     del /q /s %~dp0\%%~ni
  ) else (
     echo %%~ni�ļ��в�����,ִ�д���
     md %~dp0\%%~ni
  )
)
::�ҵ���ǰĿ¼������apk�ļ�
for %%i in (*.apk) do (
  ::��ȡ��ǰ��������
  for /f "tokens=1,* delims=_" %%a in (config.txt) do (
     echo ��������%%~ni_%%a.apk
     ::~fi��ʾ�ļ�ȫ·��
     java -jar walle-cli-all.jar put -c %%a -e count_key=%%b %%~fi %~dp0\%%~ni\%%~ni_%%a.apk
  )
)
pause