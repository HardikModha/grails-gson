package grails.plugin.gson.serialization

import com.google.gson.Gson
import grails.persistence.Entity
import grails.plugin.gson.GsonFactory
import grails.test.mixin.Mock
import spock.lang.*

@Issue('https://github.com/robfletcher/grails-gson/issues/13')
@Mock([Album, Artist])
class BidirectionalPropertySpec extends Specification {

	Gson gson
	Artist artist
	Album album1, album2, album3

	void setup() {
		gson = new GsonFactory(grailsApplication, applicationContext.pluginManager).createGson()

		artist = new Artist(name: 'David Bowie').save(failOnError: true)
		album1 = new Album(title: 'Hunky Dory', artist: artist).save(failOnError: true)
		album2 = new Album(title: 'Aladdin Sane', artist: artist).save(failOnError: true)
		album3 = new Album(title: 'Low', artist: artist).save(failOnError: true)
	}

	void 'can serialize a full graph from the owning side of a relationship'() {

		expect: 'the top-level object properties are serialized'
		println gson.toJson(artist)
		def json = gson.toJsonTree(artist)
		json.has('id')
		json.has('name')

		and: 'the collection properties are serialized'
		json.albums.size() == 3
		json.albums.every { it.has('id') }
		json.albums.every { it.has('title') }

		and: 'the circular reference is not followed'
		json.albums.every { !it.has('artist') }

	}

	void 'serialization stops when it hits a circular relationship'() {

		expect: 'the top-level object properties are serialized'
		println gson.toJson(album1)
		def json = gson.toJsonTree(album1)
		json.has('id')
		json.has('title')
		json.has('artist')

		and: 'the relationship properties are serialized'
		json.artist.has('id')
		json.artist.has('name')

		and: 'the circular reference is not followed'
		!json.artist.has('albums')

	}

}

@Entity
class Album {
	String title
	static belongsTo = [artist: Artist]
}

@Entity
class Artist {
	String name
	static hasMany = [albums: Album]
}
