<?xml version="1.0"?>
<!-- 
This style sheet is for transforming the testng.xml into a human readable HTML format.
 -->
<xsl:stylesheet version="1.0"	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<head>
				<title>XML XSL Example</title>
				<style type="text/css">
					table, td, th
					{
					border:1px solid green;
					}
					th
					{
					background-color:green;
					color:white;
					}
				</style>
			</head>
			<body>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>
	<xsl:template match="suite">
		<h2>TestNG DataProvider Suite</h2>
		<table border="1">
			<tr>
				<th>suite variable</th>
				<th>value</th>
			</tr>
			<tr>
				<xsl:value-of select="parameter" />
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="suite/test">
		<h2>Test List</h2>
		<table border="1">
			<tr>
				<th>textString1</th>
				<th>textString2</th>
			</tr>
			<xsl:for-each select="test">
				<tr>
					<xsl:for-each select="parameter">
						<td>
							<xsl:value-of select="." />
						</td>
					</xsl:for-each>
				</tr>
			</xsl:for-each>
		</table>
		<h4>The End.</h4>
	</xsl:template>	
</xsl:stylesheet>
