```xml
<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/limemc-network/*</url>
</repository>
```
```xml
<dependency>
    <groupId>net.limemc.fbp</groupId>
    <artifactId>fbp-service</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Использование Territory ::**

Позволяет легко создавать области по координатам

```java
import net.limemc.fbp.api.region.Point;
import net.limemc.fbp.api.region.Territory;
import net.limemc.fbp.api.region.TerritoryTypes;

Point a = new Point(world, x, y, z);
Point b = new Point(world, x2, y2, z2);

Territory territory
        = new Territory(TerritoryTypes.CUBE, a, b);
    
log.info( territory.getEntitiesInTerritory() );
```

**Использование FastSession ::**

```java

import net.limemc.fbp.api.FastSession;
import net.limemc.fbp.api.FastSessionBuilder;

FastSession session = FastSessionBuilder
        .builder(territory) // принимает также локацию
        .async(true)
        .removeTileEntity(true)
        .build();

session.apply(); // отправить изменения
long ms = session.flush(); // очистить ресурсы и получить потраченное время
```