# Sponge Mod

A Fabric 1.20.1 mod about sponges that dry out, soak up water, and poison you.

## What it does

- **Sponge** dries out after one in-game day and becomes a **Dried Sponge**.
- **Dried Sponge** sponges up water it touches: it becomes a **Soaked Sponge**, and the water
  around it turns into **Dirty Water**.
- **Soaked Sponge** dries back to a Dried Sponge after three days.
- **Dirty Water** is a block, not a fluid, so you can't swim in it. Standing in it poisons you:
  the longer you stood in it, the longer the poison lasts.
- **Aging Planks** rot into **Garbage Oak** after five days. Vanilla planks can't be changed
  without a mixin, so this block stands in for them.

## Building

Requires Java 17. The Gradle wrapper is included:

```
./gradlew build
```

The jar lands in `build/libs/`. Drop it and [Fabric API](https://modrinth.com/mod/fabric-api)
into your `mods` folder.

## Licence

MIT, see `LICENSE`.
