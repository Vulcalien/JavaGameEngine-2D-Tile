# Copyright 2020 Vulcalien

# Download and install the lastest version of Vulc-Engine

import sys
import os
import shutil

from zipfile import ZipFile
from urllib import request as url
from os import path

if(len(sys.argv) <= 1):
    print('Missing arguments. Try [destination]')
    sys.exit()

dest_folder = sys.argv[1]

if(path.exists(dest_folder)):
    if(not path.isdir(dest_folder)):
        print('Error: the destination exists but is not a directory')
        sys.exit()
    elif(len(os.listdir(dest_folder))):
        print('Error: the destination folder is not empty')
        sys.exit()
else:
    os.makedirs(dest_folder)

archive_file = dest_folder + '/temp_downloaded_repo_Vulc_Engine'
if(path.exists(archive_file)):
    print('Error: destination file already exists, couldn\'t download the archive')
    sys.exit()

# download master branch
print('Downloading the Engine...')
url.urlretrieve('https://www.github.com/Vulcalien/Vulc-Engine/archive/master.zip', archive_file)

# extract archive folder (temp_folder)
print('Extracting files...')
with ZipFile(archive_file, 'r') as zip_file:
    zip_file.extractall(dest_folder)
os.remove(archive_file)

# move everything from temp-folder to destination folder
temp_folder = dest_folder + '/' + os.listdir(dest_folder)[0]

IGNORED_FOLDERS = ('scripts')

files = os.listdir(temp_folder)
for f in files:
    if(not f in IGNORED_FOLDERS):
        shutil.move(temp_folder + '/' + f, dest_folder)

os.removedirs(temp_folder)

RES_FOLDERS = ('fonts', 'gfx', 'sfx')

for folder in RES_FOLDERS:
    folder_path = dest_folder + '/res/' + folder
    if(not path.exists(folder_path)):
        os.makedirs(folder_path)
