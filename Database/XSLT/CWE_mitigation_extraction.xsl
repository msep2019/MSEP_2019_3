<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cwe="http://cwe.mitre.org/cwe-6">
    <xsl:output indent="yes"/>
    
    <xsl:template match="/">
        <Weakness_Catalog>
            <Weaknesses>
                <xsl:for-each select="/cwe:Weakness_Catalog/cwe:Weaknesses/cwe:Weakness">
                    <Weakness>
                    <Related_Weaknesses>
                        <xsl:for-each select="cwe:Related_Weaknesses/cwe:Related_Weakness">
                            <Related_Weaknesses>
                               <xsl:attribute name="Nature"><xsl:value-of select="@Nature"/></xsl:attribute>
                               <xsl:attribute name="CWE_ID"><xsl:value-of select="@CWE_ID"/></xsl:attribute>
                               <xsl:attribute name="View_ID"><xsl:value-of select="@View_ID"/></xsl:attribute>
                            </Related_Weaknesses>
                        </xsl:for-each>
                    </Related_Weaknesses>
                    <Potential_Mitigations>
                        <Mitigation>
                            <Description>
                                <xsl:value-of select="cwe:Potential_Mitigations/cwe:Mitigation/cwe:Description"/>
                            </Description>
                        </Mitigation>
                    </Potential_Mitigations>
                    </Weakness>
                </xsl:for-each>      
            </Weaknesses>
        </Weakness_Catalog>
    </xsl:template>
</xsl:stylesheet>
