#!/usr/bin/python

import sys,os
import optparse
import time
from svnutils import *

timestamp = time.strftime("%Y%m%d")
parser = optparse.OptionParser("usage: %prog [options] [productdir]")
parser.add_option("-s",dest="suffix",help="The tag suffix - defaults to yyyymmdd")
#parser.add_option("-b",dest="branch",help="Use branches instead of tags",action="store_true")
parser.add_option("-t",dest="tag",help="The full tag (or version) to create - defaults to $product-$suffix")
parser.add_option("-d",dest="dry",help="Dry run - only output what I would do",action="store_true")

if len(sys.argv) == 1:
    parser.print_help()
    sys.exit(1)
    
options,args = parser.parse_args()
if options.suffix and options.tag:
    parser.print_help()
    print "Either specify a suffix OR a tag, but not both"
    sys.exit(1)

suffix = options.suffix or timestamp
productDir = len(args) and args[0] or "."
productName = os.path.split(os.path.abspath(productDir))[1]
tag = options.tag or productName + "-" + suffix

checkExternals(productDir)
message = None
if not options.dry:
    message = promptForCommitMessage() 
    if len(message.strip()) == 0:
        print "I do not permit empty commit messages"
        sys.exit(1)
info = svnInfo(productDir)
repoPath = info["URL"].replace(info["Repository Root"],"")
if repoPath.endswith("/"): repoPath = repoPath[0:-1]
repoPath = repoPath.replace("trunk/","")
productPrefix = ""
repoPathParts = repoPath.split("/")
if len(repoPathParts) > 1:
    productPrefix = "/".join(repoPathParts[:-1])
tags = locateTags( {productDir:None} )
tagDir = tags.values()[0] + "/" + productPrefix + "/" + tag 

if options.dry:
    print colorize("create tag at : "), tagDir
else:
    if not remoteDirExists(tagDir):
        makeTag(info["URL"],tagDir,"product externals tag")

externals = svn("propget","svn:externals",productDir)
if not options.dry:
    tmpDir = ".svn-tag-%s.tmp" % tag
    checkOutEmpty(tagDir,tmpDir)

#assuming no existing revisions in externals
newExternals = ""
for k in externals:
    chunks = k.split()
    if len(chunks) == 2:
        dirname,url = chunks
    else:
        dirname,url = chunks[0],chunks[3]
    info = svnInfo(dirname)
    newExternals = newExternals + "%s -r %s %s\n" % (dirname,info["Revision"],url)

if options.dry:
    print colorize("new externals would be :")
    print newExternals
else:
    setExternals(tmpDir,newExternals)
    svn("commit","-m",message,tmpDir)
