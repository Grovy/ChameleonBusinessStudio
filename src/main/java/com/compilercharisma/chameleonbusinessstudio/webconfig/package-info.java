/**
 * This package handles the customization & configuration aspects of the
 * website. I haven't settled on exactly what design I want, but this is
 * relatively close
 * 
 *             CLASS             | PURPOSE
 * ApplicationFolder             | server file system interaction
 * WebsiteAppearanceConfig       | data object, saved as JSON file
 * WebsiteAppearanceConfigLoader | set / get current config
 * WebsiteAppearanceController   | handle admin / organizer config request
 * WebsiteAppearanceService      | used by controller to set config contents
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
package com.compilercharisma.chameleonbusinessstudio.webconfig;
