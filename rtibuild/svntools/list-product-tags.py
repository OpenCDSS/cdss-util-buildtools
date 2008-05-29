import svnutils
import sys 

def color(message):
    color = "43;30" 
    print "\033[%s;1m%s\033[0m" % (color,message)

productDir = len(sys.argv) > 1 and sys.argv[1] or "."
print "finding external tags...",
externals = svnutils.getExternals(productDir)
print " done"
for val in svnutils.locateTags(externals).values():
    color( "repository: %s"%val)
    for item in svnutils.svn("ls",val):
        print "\t%s" % item 


