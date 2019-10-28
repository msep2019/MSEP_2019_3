<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cwe="http://cwe.mitre.org/cwe-6">
    <xsl:output indent="yes"/>
    
    <xsl:template match="/">
        <Weakness_Catalog>
            <Weaknesses>
                <xsl:for-each select="/cwe:Weakness_Catalog/cwe:Weaknesses/cwe:Weakness">
                    <Weakness seq="1999-0001" type="CAN">
                        <xsl:attribute name="ID"><xsl:value-of select="@ID"/></xsl:attribute>
                        <xsl:attribute name="Name"><xsl:value-of select="@Name"/></xsl:attribute>
                        <xsl:attribute name="Abstraction"><xsl:value-of select="@Abstraction"/></xsl:attribute>
                        <xsl:attribute name="Structure"><xsl:value-of select="@Structure"/></xsl:attribute>
                        <xsl:attribute name="Status"><xsl:value-of select="@Status"/></xsl:attribute>
                    <Potential_Mitigations>
                        <xsl:for-each select="cwe:Potential_Mitigations/cwe:Mitigation">
                        <Mitigation>
                            <Description>
                                <xsl:value-of select="cwe:Description"/>
                            </Description>
                        </Mitigation>
                        </xsl:for-each>
                    </Potential_Mitigations>
                    </Weakness>
                </xsl:for-each>      
            </Weaknesses>
        </Weakness_Catalog>
    </xsl:template>
</xsl:stylesheet>
