import os

#多行命令执行，使用&分隔
cmd = '''e: & \
cd e:/android analysis & \
d:/android/sdk/tools/android create uitest-project -n UIAutomator -t 37 -p \"e:/android analysis/UIAutomator\" & \
cd UIAutomator & \
ant build & \
cd bin & \
adb push UIAutomator.jar /data/local/tmp/ & \
adb shell uiautomator runtest UIAutomator.jar -c com.bertram.test.RegisterActivity
'''
os.system(cmd)


