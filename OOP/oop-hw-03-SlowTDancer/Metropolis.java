public class Metropolis {
    private String metropolis, continent, population;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Metropolis other)) throw new RuntimeException("Can't compare Spot to " + obj.getClass());
        return other.metropolis.equals(this.metropolis) && other.continent.equals(this.continent) && other.population.equals(this.population);
    }

    public Metropolis(String metropolis, String continent, String population){
        this.metropolis = metropolis;
        this.continent = continent;
        this.population = population;
    }
    public String get(int index){
        return switch (index) {
            case 0 -> metropolis;
            case 1 -> continent;
            case 2 -> population;
            default -> throw new RuntimeException("index out of bound exception");
        };
    }

    public String getMetropolis(){
        return metropolis;
    }

    public String getContinent(){
        return continent;
    }

    public String getPopulation(){
        return population;
    }
}
