<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:capec="http://capec.mitre.org/capec-3">
    <xsl:output indent="yes"/>
    
    <xsl:template match="/">
        <Attack_Pattern_Catalog>
            <Attack_Patterns>
                <xsl:for-each select="/capec:Attack_Pattern_Catalog/capec:Attack_Patterns/capec:Attack_Pattern">
                <Attack_Pattern>
                    <xsl:attribute name="ID"><xsl:value-of select="@ID"/></xsl:attribute>
                    <xsl:attribute name="Name"><xsl:value-of select="@Name"/></xsl:attribute>
                    <xsl:attribute name="Abstraction"><xsl:value-of select="@Abstraction"/></xsl:attribute>
                    <xsl:attribute name="Status"><xsl:value-of select="@Status"/></xsl:attribute>
                    <Description>
                        <xsl:value-of select="capec:Description"/>
                    </Description>
                </Attack_Pattern>
            </xsl:for-each>
            </Attack_Patterns>
        </Attack_Pattern_Catalog>
    </xsl:template>
</xsl:stylesheet>
