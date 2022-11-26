package edu.shmonin.bookshelftest.util;

import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.ReadingException;
import edu.shmonin.bookshelftest.model.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EpubToPageConverter {

    public List<Page> convert(MultipartFile file, File tempFile) {
        try {
            file.transferTo(tempFile);
            var reader = new Reader();
            reader.setMaxContentPerSection(1500);
            reader.setIsIncludingTextContent(true);
            reader.setFullContent(tempFile.getPath());

            var pages = new ArrayList<Page>();
            int position = 1;
            while (true) {
                try {
                    pages.add(new Page(position, reader.readSection(position).getSectionTextContent()));
                    position++;
                } catch (Exception e) {
                    break;
                }
            }
            tempFile.delete();
            return pages;
        } catch (IOException | ReadingException e) {
            throw new RuntimeException(e);
        }
    }
}