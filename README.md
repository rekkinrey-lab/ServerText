# ServerText

A small Paper plugin that shows custom text above every player's hotbar (the action bar).

Default text: `naw.regionpvp.com | 25907`

## Install
1. Download the built jar (see the GitHub Actions tab > latest run > Artifacts).
2. Put the jar in your server's `plugins` folder and restart.

## Customize
- In-game: `/servertext set <text>`
- Or edit `plugins/ServerText/config.yml`, then run `/servertext reload`
- Colors use MiniMessage: `<red>`, `<bold>`, `<#ff55aa>`, `<gradient:red:blue>text</gradient>`
- Placeholders: `%player%` `%online%` `%max%` `%ping%` `%world%` (PlaceholderAPI supported)

Requires Paper 1.21+ and Java 21.
