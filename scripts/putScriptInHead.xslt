<?xml version="1.0"?>
 
<xsl:stylesheet version="1.0"
  xmlns:xhtml="http://www.w3.org/1999/xhtml"
  xmlns="http://www.w3.org/1999/xhtml" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  exclude-result-prefixes="xhtml xsl xs">
 
<xsl:output method="xml" version="1.0" encoding="UTF-8" doctype-public="-//W3C//DTD XHTML 1.1//EN" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" indent="yes"/> 
 
<!-- the identity template -->
<xsl:template match="@*|node()">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>
 
<!-- template for the head section. Only needed if we want to change, delete or add nodes. In our case we need it to add a link element pointing to an external CSS stylesheet. -->
 
<xsl:template match="xhtml:head">
  <xsl:copy>
    <!-- Fenton -->
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>
 
<!-- template for the body section. Only needed if we want to change, delete or add nodes. In our case we need it to add a div element containing a menu of navigation. -->
 
<xsl:template match="xhtml:body">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>
</xsl:stylesheet>
