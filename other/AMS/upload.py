#!/usr/bin/python
# -*- coding: utf-8 -*- 

import os
import sys
import time
import datetime
import subprocess
import logging
#import dateutil.parser
import collections
import xml.etree.ElementTree
import pprint
import requests #pip install requests if you don't have it already
import tempfile
import shutil
import getopt
import json


# Kinds

K_DIR = 'dir'
K_FILE = 'file'

# Repository location types

LT_URL = 'url'
LT_PATH = 'path'



_logger = logging.getLogger('git')


class CommonClient(object):
    def __init__(self, url_or_path, type_, *args, **kwargs):
        self.__url_or_path = url_or_path
        self.__username = kwargs.pop('username', None)
        self.__password = kwargs.pop('password', None)

        if type_ not in (LT_URL, LT_PATH):
            raise ValueError("Type is invalid: %s" % (type_))

        self.__type = type_

    def run_command(self, subcommand, args, success_code=0, 
                    return_stderr=False, combine=False, return_binary=False):
        #cmd = ['svn', '--non-interactive']
        cmd = ['git']

        if self.__username is not None and self.__password is not None:
            cmd += ['--username', self.__username]
            cmd += ['--password', self.__password]

        cmd += [subcommand] + args

        _logger.debug("RUN: %s" % (cmd,))

        p = subprocess.Popen(cmd, 
                             stdout=subprocess.PIPE, 
                             stderr=subprocess.STDOUT)

        stdout = p.stdout.read()
        r = p.wait()

        if r != success_code:
            raise ValueError("Command failed with (%d): %s\n%s" % 
                             (p.returncode, cmd, stdout))

        if return_binary is True:
            return stdout

        if combine is True:
            return stdout.strip('\n')
        else:
            return stdout.decode().strip('\n').split('\n')

    def config(self, subcommand):

        return self.run_command('config', subcommand, combine=True)
        
    def getName(self):

        result = self.config(['--get', 'user.email'])
        name = result.split("@")[0]
        return name

    def status(self):

        result = self.run_command('status', ['-s'], 
                    combine=True)
        statuses = result.split("\n")

        items = []
        for status in statuses:
            if status[0:1] == ' ':
                continue
            action = status.split()[0]
            path = status.split()[1]
            if action not in ['D', 'M', 'A']:
                continue

            item = {'action': action, 'path': path}
            items.append(item)

        return items

    def show(self, path):
        result = self.run_command('show', ['HEAD:'+path], 
                    combine=True)
        return result

class LocalClient(CommonClient):
    def __init__(self, path_, *args, **kwargs):
        if os.path.exists(path_) is False:
            raise EnvironmentError("Path does not exist: %s" % (path_))

        super(LocalClient, self).__init__(
            path_, 
            #svn.constants.LT_PATH, 
            LT_PATH, 
            *args, **kwargs)

    def __repr__(self):
        return ('<SVN(LOCAL) %s>' % (self.path))

# upload file to server
def upload(path, issueId, version):

    pprint.pprint("uploading file " + path)
    url = 'http://10.69.205.18:5000/api/upload'
    if os.path.isfile(path) is True:
        files = {'file':open(path)} #'file' => name of html input field
        data = {'issueId':issueId, 'version':version}

        r = requests.post(url, files=files, data=data)

# copy file to tmp issue dir
def copy(items, issueId, issueDir):

    for item in items:
        path = item['path']
        action = item['action']

        basename = os.path.basename(path)
        if basename[0:1] == '.':
            continue

        relativePath = path.replace('/', '_')
        pprint.pprint(relativePath)
        if action == 'M' or action == 'D':
            oldFileContent = r.show(path)
            oldPath = os.path.join(issueDir, relativePath)

            file = open(oldPath, "w")
            file.write(oldFileContent)
            file.close()

            upload(oldPath, issueId, 0)

        if action == 'D':
            continue

        newPath = os.path.join(issueDir, relativePath)
        shutil.copyfile(path, newPath)
        upload(newPath, issueId, 1)

def getIssueId(name, message):
    issueUrl = 'http://10.69.205.18:5000/api/getIssueId'
    issueData = {'username':name, 'message':message}
    r = requests.post(issueUrl, data=issueData)
    if r.status_code != 200:
        print('server error')
        sys.exit(2)

    response = json.loads(r.text)
    if response['code'] != 1:
        print(response['message'])
        sys.exit(2)

    if not response['data'].has_key("issue_id"):
        print('get issue id failed')
        sys.exit(2)

    return str(response['data']["issue_id"])

def usage():
    print("usage: python upload.py [option] ... [-h help | -v version | -m message]")
    print("-h this help")
    print("-v version")
    print("-m comment")

def version():
    print("Crt 0.1.1")


path = sys.path[0]

r = LocalClient(path)

def main():
    try:
        opts, args = getopt.getopt(sys.argv[1:], 'm:i:h', ['message=', 'issue=', 'help'])
    except getopt.GetoptError:
        usage()
        sys.exit(2)

    message = None

    for opt, arg in opts:
        if opt in ('-h', '--help'):
            usage()
            sys.exit(2)
        if opt in ('-v', '--version'):
            version()
            sys.exit(2)
        elif opt in ('-m', '--message'):
            message = arg
            message = message.decode('gb2312')
        elif opt in ('-i', '--issue'):
            issue = arg
            print(issue)
        else:
            usage()
            sys.exit(2)

    if message is None or len(message) == 0:
        print("message is empty")
        usage()
        sys.exit(2)


    name = r.getName()
    if name == '':
        print("get name failed, please config git")
        sys.exit(2)

    items = r.status()

    issueId = getIssueId(name, message)
    print(issueId)

    tmpDir = tempfile.gettempdir()
    issueDir = os.path.join(tmpDir, 'crttmp' + issueId)
    if os.path.exists(issueDir) is False:
        os.mkdir(issueDir)

    copy(items, issueId, issueDir)

    shutil.rmtree(issueDir)

    print("issue: http://10.69.205.18:5000/issue/" + issueId)

if __name__ == '__main__':
    main()
