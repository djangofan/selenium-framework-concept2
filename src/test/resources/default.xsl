<?xml version="1.0"?>
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
	<xsl:template match="suite/tests">
		<h2>TestNG DataProvider Suite</h2>
		<table border="1">
			<tr>
				<th>
					<xsl:value-of select="suiteName" />
				</th>
			</tr>
			<tr>
				<xsl:value-of select="suiteUrl" />
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="suite/tests">
		<h2>Test List</h2>
		<table border="1">
			<tr>
				<th>enabled</th>
				<th>testname</th>
				<th>environment</th>
				<th>testLocale</th>
				<th>browser</th>
				<th>url</th>
				<th>earth</th>
				<th>air</th>
				<th>fire</th>
				<th>water</th>
			</tr>
			<xsl:for-each select="test">
				<tr>
					<xsl:for-each
						select="enabled|testname|environment|testlocale|browser|url|earth|air|fire|water">
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
