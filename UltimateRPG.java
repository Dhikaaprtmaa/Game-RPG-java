import java.util.Random;
import java.util.Scanner;

abstract class Character {
    String name;
    int level;

    public Character(String name, int level) {
        this.name = name;
        this.level = level;
    }

    abstract void attack(Character target);

    abstract void specialAttack(Character target);
}

class Player extends Character {
    int experience;
    int maxHealth;
    int currentHealth;
    int damage;
    int gold;
    int potions;
    Weapon weapon;

    public Player(String name) {
        super(name, 1);
        this.experience = 0;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.damage = 20;
        this.gold = 0;
        this.potions = 3;
        this.weapon = new Weapon("Sword", 15);
    }

    @Override
    void attack(Character target) {
        if (target instanceof Enemy) {
            System.out.println(name + " menyerang " + target.name + " dengan " + weapon.name + "!");
            int damageDealt = new Random().nextInt(damage + weapon.damage) + 1;
            ((Enemy) target).currentHealth -= damageDealt;
            System.out.println(target.name + " menerima " + damageDealt + " poin damage.");

            if (((Enemy) target).currentHealth <= 0) {
                System.out.println(target.name + " dikalahkan!");
                experience += target.level * 10;
                levelUp();
                loot((Enemy) target);
            }
        } else {
            System.out.println("Anda hanya dapat menyerang musuh (Enemy).");
        }
    }

    @Override
    void specialAttack(Character target) {
        if (target instanceof Enemy) {
            int specialDamage = weapon.damage * 2;
            System.out.println(name + " menggunakan serangan khusus dengan " + weapon.name + "!");
            ((Enemy) target).currentHealth -= specialDamage;
            System.out.println(target.name + " menerima " + specialDamage + " poin damage.");

            if (((Enemy) target).currentHealth <= 0) {
                System.out.println(target.name + " dikalahkan!");
                experience += target.level * 15;
                levelUp();
                loot((Enemy) target);
            }
        } else {
            System.out.println("Anda hanya dapat menggunakan serangan khusus pada musuh (Enemy).");
        }
    }

    private void levelUp() {
        if (experience >= level * 20) {
            level++;
            maxHealth += 10;
            damage += 5;
            currentHealth = maxHealth;
            System.out.println(name + " naik ke level " + level + "!");
        }
    }

    public void rest() {
        System.out.println(name + " istirahat dan pulihkan kesehatan.");
        currentHealth = maxHealth;
    }

    private void loot(Enemy enemy) {
        int lootGold = new Random().nextInt(enemy.level * 8) + 1;
        System.out.println("Anda mendapatkan " + lootGold + " emas dari " + enemy.name + ".");
        gold += lootGold;

        int lootPotions = new Random().nextInt(3) + 1;
        System.out.println("Anda menemukan " + lootPotions + " potion.");
        potions += lootPotions;

        int weaponChance = new Random().nextInt(100) + 1;
        if (weaponChance <= 20) {
            Weapon newWeapon = generateRandomWeapon();
            if (newWeapon.damage > weapon.damage) {
                System.out.println("Anda menemukan " + newWeapon.name + " yang lebih kuat!");
                weapon = newWeapon;
            }
        }
    }

    public void usePotion() {
        if (potions > 0) {
            System.out.println(name + " menggunakan potion dan pulihkan kesehatan.");
            currentHealth += 30;
            if (currentHealth > maxHealth) {
                currentHealth = maxHealth;
            }
            potions--;
        } else {
            System.out.println("Anda tidak memiliki potion.");
        }
    }

    public void displayStats() {
        System.out.println("Status:");
        System.out.println(name + " - Level: " + level + ", Health: " + currentHealth + "/" + maxHealth + ", Experience: " + experience + ", Gold: " + gold + ", Potions: " + potions);
        System.out.println("Senjata: " + weapon.name + " (Damage: " + weapon.damage + ")");
    }

    private Weapon generateRandomWeapon() {
        String[] weaponNames = {"Axe", "Bow", "Staff", "Dagger"};
        String randomWeaponName = weaponNames[new Random().nextInt(weaponNames.length)];
        int randomDamage = new Random().nextInt(20) + 10;

        return new Weapon(randomWeaponName, randomDamage);
    }
}

class Weapon {
    String name;
    int damage;

    public Weapon(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }
}

class Enemy extends Character {
    int currentHealth;
    int damage;

    public Enemy(String name, int level) {
        super(name, level);
        this.currentHealth = level * 30;
        this.damage = level * 10;
    }

    @Override
    void attack(Character target) {
        if (target instanceof Player) {
            System.out.println(name + " menyerang " + target.name + "!");
            int damageDealt = new Random().nextInt(damage) + 1;
            ((Player) target).currentHealth -= damageDealt;
            System.out.println(target.name + " menerima " + damageDealt + " poin damage.");

            if (((Player) target).currentHealth <= 0) {
                System.out.println(target.name + " dikalahkan!");
            }
        } else {
            System.out.println("Musuh hanya dapat menyerang pemain (Player).");
        }
    }

    @Override
    void specialAttack(Character target) {
        if (target instanceof Player) {
            int specialDamage = damage * 2;
            System.out.println(name + " menggunakan serangan khusus!");
            ((Player) target).currentHealth -= specialDamage;
            System.out.println(target.name + " menerima " + specialDamage + " poin damage.");

            if (((Player) target).currentHealth <= 0) {
                System.out.println(target.name + " dikalahkan!");
            }
        } else {
            System.out.println("Musuh hanya dapat menggunakan serangan khusus pada pemain (Player).");
        }
    }
}

public class UltimateRPG {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Selamat datang di Ultimate RPG!");
        System.out.print("Masukkan nama karakter: ");
        String playerName = scanner.nextLine();

        Player player = new Player(playerName);
        Character enemy = generateRandomEnemy();

        System.out.println("Pertarungan dimulai!\n");

        while (player.currentHealth > 0) {
            player.displayStats();
            System.out.println(enemy.name + " - Level: " + enemy.level + ", Health: " + ((enemy instanceof Player) ? ((Player) enemy).currentHealth : ((Enemy) enemy).currentHealth));
            System.out.println("\nPilih aksi:");
            System.out.println("1. Serang");
            System.out.println("2. Serangan Khusus");
            System.out.println("3. Istirahat");
            System.out.println("4. Gunakan Potion");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    player.attack(enemy);
                    if (enemy instanceof Enemy && ((Enemy) enemy).currentHealth > 0) {
                        enemy.attack(player);
                    } else {
                        enemy = generateRandomEnemy();
                    }
                    break;
                case 2:
                    player.specialAttack(enemy);
                    if (enemy instanceof Enemy && ((Enemy) enemy).currentHealth > 0) {
                        enemy.attack(player);
                    } else {
                        enemy = generateRandomEnemy();
                    }
                    break;
                case 3:
                    player.rest();
                    enemy.attack(player);
                    break;
                case 4:
                    player.usePotion();
                    enemy.attack(player);
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Coba lagi.");
                    break;
            }
        }

        System.out.println("Game over. Terima kasih telah bermain!");
        scanner.close();
    }

    private static Character generateRandomEnemy() {
        String[] enemyNames = {"Goblin", "Skeleton", "Orc", "Troll"};
        String randomEnemyName = enemyNames[new Random().nextInt(enemyNames.length)];
        int randomEnemyLevel = new Random().nextInt(3) + 1;

        return new Enemy(randomEnemyName, randomEnemyLevel);
    }
}
