<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : nbproj.xsl
    Created on : August 13, 2008, 8:33 AM
    Author     : iws
    Description:
        Convert test-single action to run-single
        
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml"/>
    <xsl:output encoding="UTF-8"/>
    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="@name[.='test.single']">
        <xsl:attribute name="name" >
            <xsl:text>run.single</xsl:text>
        </xsl:attribute>
    </xsl:template>
</xsl:stylesheet>
