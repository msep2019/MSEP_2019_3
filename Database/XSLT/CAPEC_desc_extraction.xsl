<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cve="http://cve.mitre.org/cve/downloads/1.0">
    <xsl:output indent="yes"/>
    
    <xsl:template match="/">
        <cve>
            <xsl:for-each select="/cve:cve/cve:item">
                <item seq="1999-0001" type="CAN">
                    <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
                    <xsl:attribute name="seq"><xsl:value-of select="@seq"/></xsl:attribute>
                    <xsl:attribute name="type"><xsl:value-of select="@type"/></xsl:attribute>
                    <desc>
                        <xsl:value-of select="cve:desc"/>
                    </desc>
                </item>
            </xsl:for-each>
        </cve>
    </xsl:template>
</xsl:stylesheet>
