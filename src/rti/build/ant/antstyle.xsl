<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" indent="yes"/>
    
    <xsl:template match="/">
        <html>
            <head>
                <title>Ant Documentation</title>
            </head>
            <style>
                table {
                border-width:0;
                border-collapse: collapse;
                }
                .header td {
                border-width:1;
                border-color:black;
                background-color: gray;
                border-style: solid;
                }
                .row td {
                border-width:1;
                border-color:black;
                border-style: solid;
                vertical-align: top;
                }
            </style>
            <body>
                Properties used in build files:
                <table>
                    <tr class="header">
                        <td>Property Name</td>
                        <td>Default Value</td>
                        <td>Description</td>
                    </tr>
                    <xsl:apply-templates select="//target[@name='-init-props']/property[@name]">
                        <xsl:sort select="@name"/>
                    </xsl:apply-templates>
                </table>
                Targets:
                <table>
                    <tr class="header">
                        <td>Target</td>
                        <td>Project</td>
                        <td>Depends</td>
                        <td>Description</td>
                    </tr>
                    <xsl:apply-templates select="//target[not(starts-with(@name,'-'))]">
                        <xsl:sort select="@name"/>
                    </xsl:apply-templates>
                </table>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="property">
        <tr class="row">
            <td><xsl:value-of select="@name"/></td>
            <!--
            <td><xsl:value-of select="translate(@value,',',' ')"/></td>
            -->
            <td><xsl:call-template name="split"><xsl:with-param name="arg" select="@value"/></xsl:call-template></td>
            <td><xsl:value-of select="@description"/></td>
        </tr>
    </xsl:template>
    
    <xsl:template match="target">
        <tr class="row">
            <td><xsl:value-of select="@name"/></td>
            <td><xsl:value-of select="ancestor::project/@name"/></td>
            <td><xsl:value-of select="translate(@depends,',',' ')"/></td>
            <td><xsl:value-of select="@description"/></td>
        </tr>
    </xsl:template>
    
    <xsl:template name="split">
        <xsl:param name="arg"/>
        <xsl:choose>
            <xsl:when test="contains($arg,',')">
                <xsl:value-of select="substring-before($arg,',')"/>
                ,<br/>
                <xsl:call-template name="split">
                    <xsl:with-param name="arg" select="substring-after($arg,',')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$arg"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>
