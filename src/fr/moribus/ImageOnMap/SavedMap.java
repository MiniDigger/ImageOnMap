package fr.moribus.ImageOnMap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;

public class SavedMap 
{
	ImageOnMap plugin;
	String nomImg, nomJoueur;
	short idMap;
	BufferedImage image;
	
	SavedMap(ImageOnMap plug, String nomJ, short id, BufferedImage img)
	{
		plugin = plug;
		nomJoueur = nomJ;
		idMap = id;
		image = img;
		nomImg = "map" + id;
	}
	
	SavedMap(ImageOnMap plug, short id)
	{
		idMap = id;
		plugin = plug;
		Set<String> cle = plugin.getCustomConfig().getKeys(false);
		int tmp = 0;
		for (String s: cle)
		{
			if(plugin.getCustomConfig().getStringList(s).size() >= 3 && Short.valueOf(plugin.getCustomConfig().getStringList(s).get(0)) == id)
			{
				tmp++;
				//System.out.println(tmp);
				//MapView carte = Bukkit.getMap(Short.parseShort(plugin.getConfig().getStringList(s).get(0)));
				nomImg = plugin.getCustomConfig().getStringList(s).get(1);
				nomJoueur = plugin.getCustomConfig().getStringList(s).get(2);
				try {
					image = ImageIO.read(new File("./plugins/ImageOnMap/Image/"+ nomImg + ".png"));
					//System.out.println("Chargement de l'image fini");
				} catch (IOException e) {
					System.out.println("Image "+ nomImg +".png doesn't exists in Image directory.");
				}
			}
		}
		if(tmp == 0)
		{
			System.out.println("No map was loaded");
		}
	}
	
	Boolean SaveMap()
	{
		System.out.println("Saving map " + idMap);
		
		// Enregistrement de l'image sur le disque dur
		try
        {
                File outputfile = new File("./plugins/ImageOnMap/Image/"+ nomImg + ".png");
                ImageIO.write(MapPalette.resizeImage(image), "png", outputfile);
        } catch (IOException e)
        {
                e.printStackTrace();
                return false;
        }
		// Enregistrement de la map dans la config
		ArrayList<String> liste = new ArrayList<String>();
		liste.add(String.valueOf(idMap));
		liste.add(nomImg);
		liste.add(nomJoueur);
		plugin.getCustomConfig().set(nomImg, liste);
		plugin.saveCustomConfig();
		return true;
	}
	
	@SuppressWarnings("deprecation")
	Boolean LoadMap()
	{
		MapView carte = Bukkit.getMap(idMap);
		if(carte != null)
		{
			ImageRenderer.SupprRendu(carte);
			carte.addRenderer(new Rendu(image));
			return true;
		}
		else
			return false;
	}
	
}
