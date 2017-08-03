<#ftl strip_whitespace=true>
<#--
 * message as m
 *
 * Macro to translate a message code into a message.
 -->
<#macro m code>${springMacroRequestContext.getMessage(code)}</#macro>

<#--
 * messageText as mt
 *
 * Macro to translate a message code into a message,
 * using the given default text if no message found.
 -->
<#macro mt code, text>${springMacroRequestContext.getMessage(code, text)}</#macro>

<#--
 * messageArgs as ma
 *
 * Macro to translate a message code with arguments into a message.
 -->
<#macro ma code, args>${springMacroRequestContext.getMessage(code, args)}</#macro>

<#--
 * messageArgsText as mat
 *
 * Macro to translate a message code with arguments into a message,
 * using the given default text if no message found.
 -->
<#macro mat code, args, text>${springMacroRequestContext.getMessage(code, args, text)}</#macro>

<#--
 * theme
 *
 * Macro to translate a theme message code into a message.
 -->
<#macro theme code>${springMacroRequestContext.getThemeMessage(code)}</#macro>

<#--
 * themeText
 *
 * Macro to translate a theme message code into a message,
 * using the given default text if no message found.
 -->
<#macro themeText code, text>${springMacroRequestContext.getThemeMessage(code, text)}</#macro>

<#--
 * themeArgs
 *
 * Macro to translate a theme message code with arguments into a message.
 -->
<#macro themeArgs code, args>${springMacroRequestContext.getThemeMessage(code, args)}</#macro>

<#--
 * themeArgsText
 *
 * Macro to translate a theme message code with arguments into a message,
 * using the given default text if no message found.
 -->
<#macro themeArgsText code, args, text>${springMacroRequestContext.getThemeMessage(code, args, text)}</#macro>
