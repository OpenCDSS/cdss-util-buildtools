                                 R E A D   M E

DESCRIPTION:
------------
A-PDF Merger Command line(PMCMD) is a windows console utility that merges two 
or more Acobat PDF or image files into one PDF document. PMCMD is a stand alone
program, does not need Adobe Acrobat.

USAGE:
------
PMCMD <destination> <source file list> [Options]

   destination : Specifies the name for the new file.
   source file list: There are two way to specifies the files to be merged.
                    1, use source file name, Accept wildcards(* and ?),Example:
                         conver.pdf content*.pdf
                    2, use the -F<file list>. <File list> is a text file which
                       contains file names to be merged.One file name per line.

   Options: All the options change the destination(output) file.
     -B : Option for merging bookmark. Default is NO bookmark.
     -P<setting> : Option for changing the properties. Default is blank. The
                   <setting> is a quote string, an example is: "TITLE=A good
                   manual for beginner,AUTHOR=Bill".
                   setting syntax: KEY=VALUE, every setting split by comma.
                   Available keys are:
                     TITLE: Special the title of output pdf document.
                     SUBJECT: Special the subject of output pdf document.
                     AUTHOR: Special the author of output pdf document.
                     KEYWORDS: Special the keywords of output pdf document.
     -S<setting> : Option for change the security. Default is No security. The
                   <setting> is a quote string, an example is:"RESTRICTPASSWORD
                   =pASSwOrD096,PRINT=low,FILLFIELDS=Y".
                   setting syntax: KEY=VALUE, every setting split by comma.
                   Available keys are:
                     OPENPASSWORD: Setting the open password.
                     RESTRICTPASSWORD: Setting the restrict(owner) password.
                     PRINT: Setting print allowed. Default is none. Available
                            values are: none, low, high.
                     CHANGE: Changing the document. Values are: Y,N
                     COPY: Content copying or extraction.Values are:Y,N
                     COMMENT: Commenting. Values are: Y,N
                     FILLFIELDS: Filling of form fields. Values are: Y,N
                     COPYACCESS: Content extraction for accessibility. Values 
                                 are:Y,N
                     ASSEMBLE: Document assembly. Values are: Y,N
     -N<setting> : Option for adding page number to pdf. Default is NO page
                   number. The <setting> is a quote string. an example is:
                   "COLOR=0000FF,STYLE=[0]/[9]".
                   setting syntax: KEY=VALUE, every setting split by comma.
                   Available keys:
                     PAGEBEGIN: First page will be added number. default is 1
                     PAGEEND: Last page will be added number. default is 999999
                     OFFSET: Page number offset. default is 1
                     COLOR: RGB color. Default is 000000, means black
                     FONT: Font name, support standard font and true type font. 
                           Default is Helvetica. Standard fonts are:
                             Courier,CourierBold,CourierBoldOblique,
                             CourierOblique,Helvetica,HelveticaBold,
                             HelveticaBoldOblique,HelveticaOblique,
                             TimesRoman,TimesBold,TimesItalic,TimesBoldItalic,
                             Symbol,ZapfDingbats
                     FONTSIZE: Font size with point. Default is 12.
                     POSITION: Page number position. Available value is: left,
                               middle,right. Default is middle.
                     STYLE: Page number style.
                              [0] means numeric page number.
                              [9] means numeric total page.
                              [a] manes lowercase roman numerals page number.
                              [z] means lowercase roman numerals total page.
                              [A] means capital ROMAN numerals page number.
                              [Z] manes capital ROMAN numerals total page.
                             Example, style=[0]/[9], the page number in 3rd
                                      page will be "3/100" (100 is total pages).
                             Example2, style=page [A] of [Z], the page number 
                                       in 3 page will be "page III of C".
     -V<setting> : Option for viewer preferences. The <setting> is a quote string,
                   an example is: "OPENSHOW=bookmark,OPENPAGE=5".
                   Setting syntax: KEY=VALUE, every setting split by comma.
                   Available keys:
                     OPENSHOW: Which panes and tabs are displayed in the
                               application window by default. Available value
                               is Page,Bookmark,Thumbnail. Default is: Page.
                     ZOOM: Sets the zoom level the document will appear at when
                           opened. The value range is -1,-2 and 1 to 1600.
                           -1 means fit window
                           -2 means fit width
                           other value means percent. 100 means 100%, actual size.
                     OPENPAGE: Sets the page that the document opens at.
                               Default is 1.
                     WINDOWRESIZE: Adjusts the document window to fit snugly around
                                   the opening page. Values are: Y,N
                     WINDOWCENTER: Positions the window in the center of the screen
                                   area. Values are: Y,N
                     WINDOWFULLSCREEN: Maximizes the document window and displays
                                       the document without the menu bar, toolbar,
                                       or window controls. Values are: Y,N
                     HIDEMENU: Hides the menu of interface. Values are: Y,N
                     HIDETOOLBAR: Hides the toolbar of interface. Values are: Y,N
                     HIDEWINDOWCONTROLS: Hides the window controls of interface.
                                         Values are: Y,N
     -O : Option for adding a blank page for each document if the total page
          number for current PDF is odd.
     -T<setting> : Create a linkable table of contents from bookmarks. The <setting>
                   is a quote string, an example is: "PAGE=2,TEMPLATE=c:\t\tem.pdf".
                   Setting syntax: KEY=VALUE, every setting split by comma.
                     PAGE: Insert TOC to the page. Default is 1.
                     TEMPLATE: File name of TOC template. If not special, will use a
                               blank page.
                     LEVEL: The maximal level of bookmarks will be converted. Default
                            is all level to be converted.
                     LEFT,TOP,RIGHT,BOTTOM: Position of TOC. The unit is inch.
                     GAP: Gap between TOC items. The unit is inch.
                     LEVELSIZE[N]: TOC item size of the level N. The unit is
                                   point(1/72 inch). Default is 18. Examples:
                                   "LEVELSIZE1=20,LEVELSIZE2=16,LEVELSIZE3=12".
                     LEVELCOLOR[N]: TOC item color of the level N. The format is
                                    BBGGRR. Default is 000000, means black. Examples:
                                    "LEVELCOLOR1=0000FF,LEVELCOLOR2=FFFFFF"
Return code:
  0: Merge successful
  1: Merge failed
  2: Parameters' wrong
  3: One of sources file does not exist
  8: Unexpected error.

COPYRIGHT:
----------
Version 2.5.1 for riverside.com
Copyright by Winston Zhang, A-PDF.com