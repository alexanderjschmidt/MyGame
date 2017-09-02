package std.deviation.gameworld;

public enum Location
{

    ForestRoad(new String[]
    { "NORTHEAST" }), ForestRoad2(new String[]
    { "WEST", "SOUTH" }), ForestRoadHouse(new String[]
    { "EAST" });

    private String[] nodeNames;

    private Location(String[] nodeNames)
    {
        this.nodeNames = nodeNames;
    }

}
