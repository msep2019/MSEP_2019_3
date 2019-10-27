<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output indent="yes"/>
    
    <xsl:template match="/">
        <Security_Patterns>
            <xsl:for-each select="Security_Patterns/Security_Pattern">
                <Security_Pattern>
                    <xsl:attribute name="id"><xsl:value-of select="@name"/></xsl:attribute>
                    <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
                    <Quick_info>
                       <Content>                    
                           <xsl:value-of select="Quick_info/Content"/>                   
                       </Content>
                   </Quick_info>
                </Security_Pattern>
            </xsl:for-each>
        </Security_Patterns>
    </xsl:template>
    <xsl:template match="Mitigations">
        <xsl:copy-of select="*"/>
    </xsl:template>
</xsl:stylesheet>
