#!/usr/bin/python

import sys,os
import optparse
import time

_debug=0

def colorize(out):
    # 32 = color, 1 = bold, 4 = underline
    return '\033[32;1;4m%s\033[m' % out
 
def checkExternals(productDir):
  """Check a top level product dir to see if the externals are
  modified or not. Will exit if modified and spit out diff.
  args - productDir - the path to the directory"""
  if findStatus(productDir,productDir):
    print "directory properties not clean"
    print "clean this up to continue"
    output(dirdiff(productDir))
    sys.exit(1)
  
def output(lines):
  """Utility to print a sequence line by line"""
  for l in lines:
    print l

def findStatus(dir,path):
  """Check if the status of the item 'path' in the directory 'dir'
  is anything but uptodate. Returns the status character for the path"""
  for line in svn("status","-N",dir):
    tokens = line.split()
    if tokens[1] == path:
      return tokens[0]

def dirdiff(dir):
  """Perform a svn diff -N on 'dir' returning the output as a list""" 
  return svn("diff","-N",dir)

def debug(*s):
  if _debug:
    print "debug",s

def svn(*args):
  """Run some svn command, returning all output lines stripped of
  ending newline and excluding any blank output"""
  cmd = "svn %s 2>&1" % " ".join(map(lambda a: '"%s"' % a,args))
  p = os.popen(cmd)
  output = filter(lambda line: len(line) and line or None, (i[:-1] for i in p))
  if p.close():
    raise Exception("Error executing '%s',\n%s" % (cmd,output))
  return output

def makedict(lines,split=None):
  """Split a list of text returning a dict of key/values based on the
  first two items in the resulting split"""
  d = {}
  for line in lines:
    keyValue = line.split(split)
    if len(keyValue) > 1:
      d[keyValue[0]] = keyValue[1]
  return d

def getExternals(productDir):
  """Return a list of externals for a directory"""
  externals = makedict(svn("propget","svn:externals",productDir))
  debug("externals",externals)
  return externals

def svnInfo(dir,quiet = 0):
  """Return a dict of svn info on the given directory"""
  return makedict(svn("info",dir),split=": ")

def remoteDirExists(url):
  """Test to see if a remote URL exists and is a directory.
  Returns true or false."""
  try:
    return svnInfo(url)["Node Kind"] == "directory"
  except:
    pass

def locateTags(externals):
  """Using a dict of local->remote entries, return a dict
  of local->remote entries where the computed remote value
  is the location of tags for that repository"""
  tags = {}
  locations = ("tags","java_142/tags")
  for local,remote in externals.items():
    root = svnInfo(local)["Repository Root"]
    debug("locateTags.root",root)
    for loc in locations:
      test = root + "/" + loc 
      if remoteDirExists(test):
        tags[local] = test 
        break;
  return tags

def checkOutEmpty(url,dest):
  svn("co","--depth","empty",url,dest)

def setExternals(path,externals):
  svn("propset","svn:externals",externals,path)

def makeTag(src,dest,message):
  """Create a tag in the repository 'dest', copying from 'src'"""
  if remoteDirExists(dest):
    print "ignoring tag creation in %s, it already exists" % dest
  else:
    svn("copy","-m",message,src,dest)

def tagExternals(externals,tag,message,dry):
  """Find all externals, tag them and create a tag for the
  release"""
  tags = locateTags(externals)
  debug("tags",tags)
  missing = set(externals.keys()) - set(tags.keys())
  if missing: 
    print "unable to locate all tags : "
    output(["\tlocal %s : remote %s" % (local,externals[local]) for local in missing]) 
  else:
    if dry: print "would tag the following :"
    for local,remote in externals.items():
      fulltag = tags[local] + "/" + local + "-" + tag
      print remote + " -> " + fulltag
      if not dry:
        makeTag(remote,fulltag,message)

def promptForCommitMessage():
  print "Enter a commit message (enter one empty line to finish):"
  message = ""
  while 1:
    line = sys.stdin.readline()
    if line == '\n': break
    message += line
  return message

