package club.endi.schemastash.bukkit

import org.bukkit.plugin.java.JavaPlugin
import java.net.URI
import java.net.http.HttpRequest

class SchemastashBukkit : JavaPlugin() {
    private var apiURL: URI? = null

    override fun onEnable() {
        this.saveDefaultConfig()
        this.apiURL = this.config.getString("api-url")?.let { URI(it) }
        if (this.apiURL == null) {
            this.logger.severe("api-url is not set in config.yml")
            this.server.pluginManager.disablePlugin(this)
            return
        }

        // try sending a request to the API
        val request = HttpRequest.newBuilder()
            .uri(this.apiURL)
            .GET()
            .build()
        val response = java.net.http.HttpClient.newHttpClient().send(request, java.net.http.HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 404) {
            this.logger.severe("API unavailable at $apiURL")
            this.server.pluginManager.disablePlugin(this)
            return
        }

        this.logger.info("Schemastash Bukkit Companion Plugin enabled for $apiURL")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
